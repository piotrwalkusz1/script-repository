import {Component, OnInit, Input} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Collection, Privacy} from '../../entities/collection';
import {RepositoryService} from '../repository.service';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {JhiAlertService} from 'ng-jhipster';
import * as $ from 'jquery';

@Component({
    selector: 'jhi-repository-collection-edit',
    templateUrl: './repository-collection-edit.component.html',
    styles: []
})
export class RepositoryCollectionEditComponent implements OnInit {

    @Input() id: number;

    collection: Collection;
    isSaving = false;

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
            );
        } else {
            this.repositoryService.saveCollection(this.collection).subscribe(
                (res: HttpResponse<Collection>) => {
                    this.isSaving = false;
                    this.router.navigateByUrl('/repository/' + res.body.id );
                }
            );
        }
    }

    cancel() {
        if (this.id) {
            this.router.navigateByUrl('/repository/' + this.id);
        }
        else {
            this.router.navigateByUrl('/repository');
        }
    }

    ngOnInit() {
        if (this.id != null) {
            this.repositoryService.getCollection(this.id).subscribe(
                (res: HttpResponse<Collection>) => {
                    this.collection = res.body;
                    this.collection.privacy = this.getPrivacyFromString(this.collection.privacy)
                },
                (res: HttpErrorResponse) => {
                    this.onError(res.message);
                }
            );
        } else {
            this.collection = new Collection();
            this.collection.privacy = Privacy.PRIVATE;
        }
    }

    getPrivacies(): [Privacy] {
        return [Privacy.PRIVATE, Privacy.PUBLIC];
    }

    getPrivacyString(privacy: Privacy): string {
        switch (privacy) {
            case Privacy.PUBLIC: return 'PUBLIC';
            case Privacy.PRIVATE: return 'PRIVATE';
            default: throw RangeError('This privacy does not exists');
        }
    }

    getPrivacyFromString(privacy): Privacy {
        switch (privacy) {
            case 'PUBLIC': return Privacy.PUBLIC;
            case 'PRIVATE': return Privacy.PRIVATE;
            default: throw RangeError('This privacy does not exists');
        }
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
