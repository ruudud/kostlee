#!/usr/bin/python
#coding: utf-8
import csv
import dateutil.parser
import datetime
from decimal import Decimal

def main():
    data = []
    ifp = open('/srv/leemoney.dat', 'rb')
    aday = datetime.timedelta(days=1)
    reader = csv.reader(ifp)
    for row in reader:
        data.append(row)
        if len(data) == 1:
            continue

        prev = data[-2]
        this_date = dateutil.parser.parse(row[0]).date()
        prev_date = dateutil.parser.parse(prev[0]).date()
        if (prev_date + aday) == this_date:
            increase = Decimal(row[1]) - Decimal(prev[1])
            prev.append(increase)
        else:
            prev.append(Decimal(0))

    if not len(data) > 0:
      return

    data[-1].append(Decimal(0))

    ofp = open('/srv/leemoney.csv', 'wb')
    writer = csv.writer(ofp, delimiter=',', quotechar='"',
                        quoting=csv.QUOTE_ALL)
    for row in data:
        writer.writerow(row)

    ifp.close()
    ofp.close()

if __name__ == '__main__':
    main()
