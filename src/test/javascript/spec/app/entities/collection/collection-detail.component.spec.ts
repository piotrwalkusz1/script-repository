/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ScriptRepositoryTestModule } from '../../../test.module';
import { CollectionDetailComponent } from '../../../../../../main/webapp/app/entities/collection/collection-detail.component';
import { CollectionService } from '../../../../../../main/webapp/app/entities/collection/collection.service';
import { Collection } from '../../../../../../main/webapp/app/entities/collection/collection.model';

describe('Component Tests', () => {

    describe('Collection Management Detail Component', () => {
        let comp: CollectionDetailComponent;
        let fixture: ComponentFixture<CollectionDetailComponent>;
        let service: CollectionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ScriptRepositoryTestModule],
                declarations: [CollectionDetailComponent],
                providers: [
                    CollectionService
                ]
            })
            .overrideTemplate(CollectionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CollectionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CollectionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Collection(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.collection).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
