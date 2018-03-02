import { Injectable } from '@angular/core';
import {SERVER_API_URL} from '../app.constants';
import {Observable} from 'rxjs/Observable';
import { HttpResponse, HttpClient } from '@angular/common/http';
import {Collection} from '../entities/collection';

@Injectable()
export class RepositoryService {

    private collectionsUrl = SERVER_API_URL + 'api/repository/collections';

    constructor(private httpClient: HttpClient) {}

    getAllCollections(): Observable<HttpResponse<Collection[]>> {
        return this.httpClient.get<Collection[]>(this.collectionsUrl, { observe: 'response' });
    }
}
