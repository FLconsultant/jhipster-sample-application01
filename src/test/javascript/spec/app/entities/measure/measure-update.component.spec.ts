/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { JhipsterSampleApplication01TestModule } from '../../../test.module';
import { MeasureUpdateComponent } from 'app/entities/measure/measure-update.component';
import { MeasureService } from 'app/entities/measure/measure.service';
import { Measure } from 'app/shared/model/measure.model';

describe('Component Tests', () => {
  describe('Measure Management Update Component', () => {
    let comp: MeasureUpdateComponent;
    let fixture: ComponentFixture<MeasureUpdateComponent>;
    let service: MeasureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplication01TestModule],
        declarations: [MeasureUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MeasureUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MeasureUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MeasureService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Measure(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Measure();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
