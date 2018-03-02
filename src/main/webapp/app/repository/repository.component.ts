import { Component, OnInit } from '@angular/core';
import {Collection} from '../entities/collection';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {JhiAlertService} from 'ng-jhipster';
import {RepositoryService} from './repository.service';

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
    ) {}

    ngOnInit() {
        this.loadAll();
    }

    loadAll() {
        this.repositoryService.getAllCollections().subscribe(
            (res: HttpResponse<Collection[]>) => {
                this.collections = res.body;
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
