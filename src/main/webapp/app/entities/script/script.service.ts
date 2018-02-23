import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Script } from './script.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Script>;

@Injectable()
export class ScriptService {

    private resourceUrl =  SERVER_API_URL + 'api/scripts';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/scripts';

    constructor(private http: HttpClient) { }

    create(script: Script): Observable<EntityResponseType> {
        const copy = this.convert(script);
        return this.http.post<Script>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(script: Script): Observable<EntityResponseType> {
        const copy = this.convert(script);
        return this.http.put<Script>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Script>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Script[]>> {
        const options = createRequestOption(req);
        return this.http.get<Script[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Script[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Script[]>> {
        const options = createRequestOption(req);
        return this.http.get<Script[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Script[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Script = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Script[]>): HttpResponse<Script[]> {
        const jsonResponse: Script[] = res.body;
        const body: Script[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Script.
     */
    private convertItemFromServer(script: Script): Script {
        const copy: Script = Object.assign({}, script);
        return copy;
    }

    /**
     * Convert a Script to a JSON which can be sent to the server.
     */
    private convert(script: Script): Script {
        const copy: Script = Object.assign({}, script);
        return copy;
    }
}
