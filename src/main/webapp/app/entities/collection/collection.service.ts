import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Collection } from './collection.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Collection>;

@Injectable()
export class CollectionService {

    private resourceUrl =  SERVER_API_URL + 'api/collections';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/collections';

    constructor(private http: HttpClient) { }

    create(collection: Collection): Observable<EntityResponseType> {
        const copy = this.convert(collection);
        return this.http.post<Collection>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(collection: Collection): Observable<EntityResponseType> {
        const copy = this.convert(collection);
        return this.http.put<Collection>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Collection>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Collection[]>> {
        const options = createRequestOption(req);
        return this.http.get<Collection[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Collection[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Collection[]>> {
        const options = createRequestOption(req);
        return this.http.get<Collection[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Collection[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Collection = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Collection[]>): HttpResponse<Collection[]> {
        const jsonResponse: Collection[] = res.body;
        const body: Collection[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Collection.
     */
    private convertItemFromServer(collection: Collection): Collection {
        const copy: Collection = Object.assign({}, collection);
        return copy;
    }

    /**
     * Convert a Collection to a JSON which can be sent to the server.
     */
    private convert(collection: Collection): Collection {
        const copy: Collection = Object.assign({}, collection);
        return copy;
    }
}
