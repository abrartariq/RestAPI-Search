import json
import os
import re
import hashlib
from os.path import exists

# Main Function For Lambda Service
def lambda_handler(event, context):

    # Getting Files from EFS In Sorted Order
    filelist = os.listdir("/mnt/efsas/")
    filelist.sort()

    # Extracting Parameters from Url
    rpattern = event["queryStringParameters"]["rPattern"]
    stime = event["queryStringParameters"]["sTime"]
    etime = event["queryStringParameters"]["eTime"]
    filename = event["queryStringParameters"]["fileName"]

    # Get the Range of files that satisfy the Constraint
    fileranges = getfileRange(filelist, stime, etime)

    # Default Result Val
    result_val = "Messages Not Found"
    StatusCode = 404

    # if
        # Case Search For Presence Of Single File
    # elseif
        # Case Search for Time Stamps in All Files

    if len(filename) > 0:
        if singleFileSearch(filename, stime, eTime, rpattern) == True:
            StatusCode = 200
            result_val = "Timestamp and File Present"
    elif len(filename) == 0 and len(fileranges) > 0:
        StatusCode = 200
        messagedata = searchfile(filelist, fileranges, stime, etime, rpattern)
        result = hashlib.md5(messagedata.encode())
        result_val = "Hash of Messages = " + str(result.hexdigest())

    return {"statusCode": StatusCode, "body": json.dumps(result_val)}


# Binary Code for Closest match
def binary_search(nameList, lo, hi, key):
    mid = lo + int((hi - lo) / 2)
    if lo <= hi:
        if nameList[mid][0:12] == key:
            return mid
        elif key < nameList[mid][0:12]:
            return binary_search(nameList, lo, mid - 1, key)
        elif key > nameList[mid][0:12]:
            return binary_search(nameList, mid + 1, hi, key)
    else:
        return mid


# Finding upper Limit of EndTime on File list
def eresulttoindex(filename, ktime, closeindex):
    if closeindex == 0 and filename[0:12] > ktime:
        return -99
    elif closeindex != 0 and filename[0:12] > ktime:
        return closeindex - 1
    else:
        return closeindex


# Finding Lower Limit of StartTime on File list
def sresulttoindex(filename, ktime, closeindex):
    if closeindex != 0 and filename[0:12] > ktime:
        return closeindex - 1
    return closeindex


# Getting Range of List that Satisfy the Time Window
def getfileRange(filelist, stime, etime):
    eResult = binary_search(filelist, 0, len(filelist) - 1, etime)
    eRange = eresulttoindex(filelist[eResult], etime, eResult)

    if eRange == -99:
        return []

    sResult = binary_search(filelist, 0, len(filelist) - 1, stime)
    sRange = sresulttoindex(filelist[sResult], stime, sResult)
    return list(range(sRange, eRange + 1))

# Search for Time Stamps in All Files And Concat The result
def searchfile(filelist, fileranges, stime, etime, rpattern):
    result = ""
    for val in fileranges:
        with open("/mnt/efsas/" + filelist[val], "r") as fin:
            linelist = fin.read().split("\n")
            filesize = len(linelist)
            if linelist[filesize - 2].split(" ")[0] < stime or linelist[0].split(" ")[0] > etime:
                pass
            else:
                for valx in linelist:
                    if valx != "":
                        linesplit = valx.split(" ")
                        timestamp = linesplit[0]
                        messageBody = re.findall("(\\ -\\ .*)", valx)[0][3:]

                        if timestamp >= stime and timestamp <= etime and re.fullmatch(rpattern, messageBody) != None:
                            result = result + messageBody
    return result

# Search For Single File
def singleFileSearch(filename, stime, eTime, rpattern):
    file_exists = exists("/mnt/efsas/" + filename)
    if file_exists == False:
        return False
    with open("/mnt/efsas/" + filename, "r") as fin:
        linelist = fin.read().split("\n")
        filesize = len(linelist)
        if linelist[filesize - 2].split(" ")[0] < stime or linelist[0].split(" ")[0] > etime
            return False

    return True
