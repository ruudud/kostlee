#!/usr/bin/python
#coding: utf-8
import csv
import dateutil.parser
import datetime
from decimal import Decimal

def isTertialShift(previous, current):
    return current < previous

def writeCSV(path, data):
    fp = open(path, 'wb')
    writer = csv.writer(fp, delimiter=',', quotechar='"',
                        quoting=csv.QUOTE_ALL)
    for row in data:
        writer.writerow(row)
    fp.close()

def parseDate(datestr):
    return dateutil.parser.parse(datestr).date()

def isDatesSequential(before, after):
    one_day = datetime.timedelta(days=1)
    return (before + one_day) == after

def main(inputfile, outputfile):
    outputdata = []
    fp_reader = open(inputfile, 'rb')
    reader = csv.reader(fp_reader)

    last_tertial_sum = 0
    prev_input_amount = 0

    for original in reader:
        this_row = original[:]
        outputdata.append(this_row)

        if len(outputdata) == 1:
            continue

        prev_row = outputdata[-2]
        prev_increase = Decimal(0)

        this_date = parseDate(this_row[0])
        prev_date = parseDate(prev_row[0])

        if isDatesSequential(prev_date, this_date):
            prev_sum = Decimal(prev_row[1])
            this_amount = Decimal(this_row[1])

            if (isTertialShift(prev_input_amount, this_amount)):
                last_tertial_sum += prev_input_amount

            this_row[1] = this_amount + last_tertial_sum
            prev_increase = (this_amount + last_tertial_sum) - prev_sum

        prev_row.append(prev_increase)
        prev_input_amount = Decimal(original[1])


    if not len(outputdata) > 0:
      return

    outputdata[-1].append(Decimal(0))
    fp_reader.close()
    writeCSV(outputfile, outputdata)

if __name__ == '__main__':
    main('/srv/leemoney.dat', '/srv/leemoney.csv')
