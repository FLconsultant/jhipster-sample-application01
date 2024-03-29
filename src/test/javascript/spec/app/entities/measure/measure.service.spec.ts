/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { MeasureService } from 'app/entities/measure/measure.service';
import { IMeasure, Measure } from 'app/shared/model/measure.model';

describe('Service Tests', () => {
  describe('Measure Service', () => {
    let injector: TestBed;
    let service: MeasureService;
    let httpMock: HttpTestingController;
    let elemDefault: IMeasure;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(MeasureService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Measure(0, 'AAAAAAA', 0, 0, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            measuredate: currentDate.format(DATE_FORMAT),
            measuredatetime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Measure', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            measuredate: currentDate.format(DATE_FORMAT),
            measuredatetime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            measuredate: currentDate,
            measuredatetime: currentDate
          },
          returnedFromService
        );
        service
          .create(new Measure(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Measure', async () => {
        const returnedFromService = Object.assign(
          {
            measuresource: 'BBBBBB',
            measurevalue: 1,
            measurevaluelong: 1,
            measuredate: currentDate.format(DATE_FORMAT),
            measuredatetime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            measuredate: currentDate,
            measuredatetime: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Measure', async () => {
        const returnedFromService = Object.assign(
          {
            measuresource: 'BBBBBB',
            measurevalue: 1,
            measurevaluelong: 1,
            measuredate: currentDate.format(DATE_FORMAT),
            measuredatetime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            measuredate: currentDate,
            measuredatetime: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Measure', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
