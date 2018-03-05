import {Component, OnInit, Input} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Collection} from "../../entities/collection";
import {RepositoryService} from "../repository.service";
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {JhiAlertService} from 'ng-jhipster';

@Component({
    selector: 'jhi-repository-collection-edit',
    templateUrl: './repository-collection-edit.component.html',
    styles: []
})
export class RepositoryCollectionEditComponent implements OnInit {

    @Input() id: number;

    collection: Collection;
    isSaving: boolean = false;

    constructor(route: ActivatedRoute,
                private repositoryService: RepositoryService,
                private router: Router,
                private jhiAlertService: JhiAlertService) {
        this.id = route.snapshot.params['collectionId'];
    }

    save() {
        this.isSaving = true;
        if (this.id != null) {
            this.repositoryService.updateCollection(this.collection).subscribe(
                (res: HttpResponse<void>) => {
                    this.isSaving = false;
                },
                (res: HttpErrorResponse) => {
                    this.onError(res.message);
                    this.isSaving = false;
                }
            )
        } else {
            this.repositoryService.saveCollection(this.collection).subscribe(
                (res: HttpResponse<Collection>) => {
                    this.isSaving = false
                    this.router.navigateByUrl('/repository', { queryParams: { collectionId: res.body.id } })
                }
            )
        }
    }

    ngOnInit() {
        if (this.id != null) {
            this.repositoryService.getCollection(this.id).subscribe(
                (res: HttpResponse<Collection>) => {
                    this.collection = res.body;
                },
                (res: HttpErrorResponse) => {
                    this.onError(res.message);
                }
            )
        } else {
            this.collection = new Collection();
        }
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
