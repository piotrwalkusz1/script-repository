/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { ScriptRepositoryTestModule } from '../../../test.module';
import { ScriptDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/script/script-delete-dialog.component';
import { ScriptService } from '../../../../../../main/webapp/app/entities/script/script.service';

describe('Component Tests', () => {

    describe('Script Management Delete Component', () => {
        let comp: ScriptDeleteDialogComponent;
        let fixture: ComponentFixture<ScriptDeleteDialogComponent>;
        let service: ScriptService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ScriptRepositoryTestModule],
                declarations: [ScriptDeleteDialogComponent],
                providers: [
                    ScriptService
                ]
            })
            .overrideTemplate(ScriptDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ScriptDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ScriptService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
