import { Injectable } from '@angular/core';
import {SERVER_API_URL} from '../app.constants';
import {Observable} from 'rxjs/Observable';
import { HttpResponse, HttpClient } from '@angular/common/http';
import {Collection} from '../entities/collection';
import {Script} from '../entities/script';

@Injectable()
export class RepositoryService {

    private collectionsUrl = SERVER_API_URL + 'api/repository/collections';
    private scriptsUrl = SERVER_API_URL + 'api/repository/scripts';

    constructor(private httpClient: HttpClient) {}

    getAllCollections(): Observable<HttpResponse<Collection[]>> {
        return this.httpClient.get<Collection[]>(this.collectionsUrl, { observe: 'response' });
    }

    getAllScriptsFromCollection(id: number): Observable<HttpResponse<Script[]>> {
        return this.httpClient.get<Script[]>(this.collectionsUrl + '/' + id + '/scripts', { observe: 'response' });
    }

    getScript(id: number): Observable<HttpResponse<Script>> {
        return this.httpClient.get<Script>(this.scriptsUrl + '/' + id, { observe: 'response' });
    }

    updateScript(script: Script): Observable<HttpResponse<void>> {
        return this.httpClient.put<void>(this.scriptsUrl + '/' + script.id, script,{ observe: 'response' });
    }

    saveScript(script: Script): Observable<HttpResponse<Script>> {
        return this.httpClient.post<Script>(this.scriptsUrl, script,{ observe: 'response' });
    }

    saveCollection(collection: Collection): Observable<HttpResponse<Script>> {
        return this.httpClient.post<Collection>(this.collectionsUrl, collection, { observe: 'response' });
    }

    updateCollection(collection: Collection): Observable<HttpResponse<void>> {
        return this.httpClient.put<void>(this.collectionsUrl + '/' + collection.id, collection, { observe: 'response' });
    }

    getCollection(id: number): Observable<HttpResponse<Collection>> {
        return this.httpClient.get<Collection>(this.collectionsUrl + '/' + id, { observe: 'response'});
    }
}
