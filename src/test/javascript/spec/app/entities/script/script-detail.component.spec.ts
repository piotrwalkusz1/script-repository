/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ScriptRepositoryTestModule } from '../../../test.module';
import { ScriptDetailComponent } from '../../../../../../main/webapp/app/entities/script/script-detail.component';
import { ScriptService } from '../../../../../../main/webapp/app/entities/script/script.service';
import { Script } from '../../../../../../main/webapp/app/entities/script/script.model';

describe('Component Tests', () => {

    describe('Script Management Detail Component', () => {
        let comp: ScriptDetailComponent;
        let fixture: ComponentFixture<ScriptDetailComponent>;
        let service: ScriptService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ScriptRepositoryTestModule],
                declarations: [ScriptDetailComponent],
                providers: [
                    ScriptService
                ]
            })
            .overrideTemplate(ScriptDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ScriptDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ScriptService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Script(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.script).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
