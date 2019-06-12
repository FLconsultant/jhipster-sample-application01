import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IMeasure, Measure } from 'app/shared/model/measure.model';
import { MeasureService } from './measure.service';

@Component({
  selector: 'jhi-measure-update',
  templateUrl: './measure-update.component.html'
})
export class MeasureUpdateComponent implements OnInit {
  isSaving: boolean;
  measuredateDp: any;

  editForm = this.fb.group({
    id: [],
    measuresource: [],
    measurevalue: [],
    measurevaluelong: [],
    measuredate: [],
    measuredatetime: []
  });

  constructor(protected measureService: MeasureService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ measure }) => {
      this.updateForm(measure);
    });
  }

  updateForm(measure: IMeasure) {
    this.editForm.patchValue({
      id: measure.id,
      measuresource: measure.measuresource,
      measurevalue: measure.measurevalue,
      measurevaluelong: measure.measurevaluelong,
      measuredate: measure.measuredate,
      measuredatetime: measure.measuredatetime != null ? measure.measuredatetime.format(DATE_TIME_FORMAT) : null
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const measure = this.createFromForm();
    if (measure.id !== undefined) {
      this.subscribeToSaveResponse(this.measureService.update(measure));
    } else {
      this.subscribeToSaveResponse(this.measureService.create(measure));
    }
  }

  private createFromForm(): IMeasure {
    const entity = {
      ...new Measure(),
      id: this.editForm.get(['id']).value,
      measuresource: this.editForm.get(['measuresource']).value,
      measurevalue: this.editForm.get(['measurevalue']).value,
      measurevaluelong: this.editForm.get(['measurevaluelong']).value,
      measuredate: this.editForm.get(['measuredate']).value,
      measuredatetime:
        this.editForm.get(['measuredatetime']).value != null
          ? moment(this.editForm.get(['measuredatetime']).value, DATE_TIME_FORMAT)
          : undefined
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeasure>>) {
    result.subscribe((res: HttpResponse<IMeasure>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
