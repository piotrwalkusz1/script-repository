/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ScriptRepositoryTestModule } from '../../../test.module';
import { ScriptComponent } from '../../../../../../main/webapp/app/entities/script/script.component';
import { ScriptService } from '../../../../../../main/webapp/app/entities/script/script.service';
import { Script } from '../../../../../../main/webapp/app/entities/script/script.model';

describe('Component Tests', () => {

    describe('Script Management Component', () => {
        let comp: ScriptComponent;
        let fixture: ComponentFixture<ScriptComponent>;
        let service: ScriptService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ScriptRepositoryTestModule],
                declarations: [ScriptComponent],
                providers: [
                    ScriptService
                ]
            })
            .overrideTemplate(ScriptComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ScriptComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ScriptService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Script(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.scripts[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
