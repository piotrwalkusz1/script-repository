/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { ScriptRepositoryTestModule } from '../../../test.module';
import { ScriptDialogComponent } from '../../../../../../main/webapp/app/entities/script/script-dialog.component';
import { ScriptService } from '../../../../../../main/webapp/app/entities/script/script.service';
import { Script } from '../../../../../../main/webapp/app/entities/script/script.model';
import { CollectionService } from '../../../../../../main/webapp/app/entities/collection';
import { TagService } from '../../../../../../main/webapp/app/entities/tag';

describe('Component Tests', () => {

    describe('Script Management Dialog Component', () => {
        let comp: ScriptDialogComponent;
        let fixture: ComponentFixture<ScriptDialogComponent>;
        let service: ScriptService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ScriptRepositoryTestModule],
                declarations: [ScriptDialogComponent],
                providers: [
                    CollectionService,
                    TagService,
                    ScriptService
                ]
            })
            .overrideTemplate(ScriptDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ScriptDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ScriptService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Script(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.script = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'scriptListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Script();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.script = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'scriptListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
