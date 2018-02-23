/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ScriptRepositoryTestModule } from '../../../test.module';
import { CollectionComponent } from '../../../../../../main/webapp/app/entities/collection/collection.component';
import { CollectionService } from '../../../../../../main/webapp/app/entities/collection/collection.service';
import { Collection } from '../../../../../../main/webapp/app/entities/collection/collection.model';

describe('Component Tests', () => {

    describe('Collection Management Component', () => {
        let comp: CollectionComponent;
        let fixture: ComponentFixture<CollectionComponent>;
        let service: CollectionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ScriptRepositoryTestModule],
                declarations: [CollectionComponent],
                providers: [
                    CollectionService
                ]
            })
            .overrideTemplate(CollectionComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CollectionComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CollectionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Collection(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.collections[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
