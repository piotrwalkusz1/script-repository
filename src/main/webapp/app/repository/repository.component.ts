import { Component, OnInit } from '@angular/core';
import {Collection} from '../entities/collection';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {JhiAlertService} from 'ng-jhipster';
import {RepositoryService} from './repository.service';
import {Script} from '../entities/script';
import {ActivatedRoute} from '@angular/router';

@Component({
    selector: 'jhi-repository',
    templateUrl: './repository.component.html'
})
export class RepositoryComponent implements OnInit {

    selectedCollection: Collection;
    collections: Collection[];

    constructor(
        private repositoryService: RepositoryService,
        private jhiAlertService: JhiAlertService,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.loadAll();
    }

    loadAll() {
        this.repositoryService.getAllCollections().subscribe(
            (res: HttpResponse<Collection[]>) => {
                this.collections = res.body;
                let collectionId = this.route.snapshot.params['collectionId'];
                if (collectionId !== null) {
                    this.selectedCollection = this.collections.find((col) => col.id === collectionId);
                }
            },
            (res: HttpErrorResponse) => {
                this.onError(res.message);
            }
        );
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
