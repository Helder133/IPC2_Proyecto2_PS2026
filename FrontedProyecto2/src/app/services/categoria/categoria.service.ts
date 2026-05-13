import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { RestConstants } from '../../shared/restapi/rest-constants';
import { CategoriaRequest } from '../../models/categoria/CategoriaRequest';
import { Observable } from 'rxjs';
import { CategoriaResponse } from '../../models/categoria/CategoriaResponse';
import { CategoriaUpdate } from '../../models/categoria/CategoriaUpdate';

@Injectable({
  providedIn: 'root',
})
export class CategoriaService {
  private http = inject(HttpClient);
  private apiUrl = `${new RestConstants().getApiUrl()}categoria`;

  createCategoria(req: CategoriaRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/create`, req);
  }

  getAllCategorias(): Observable<CategoriaResponse[]> {
    return this.http.get<CategoriaResponse[]>(this.apiUrl);
  }

  getActivadaCategoria(): Observable<CategoriaResponse[]> {
    return this.http.get<CategoriaResponse[]>(`${this.apiUrl}/activada`);
  }

  getCategoriaById(id: number): Observable<CategoriaResponse> {
    return this.http.get<CategoriaResponse>(`${this.apiUrl}/${id}`);
  }

  getCategoriaByCoincidence(nombre: string): Observable<CategoriaResponse[]> {
    return this.http.get<CategoriaResponse[]>(`${this.apiUrl}/coincidence/${nombre}`);
  }

  actualizarCategoria(req: CategoriaUpdate): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar`, req);
  }

  actualizarCategoriaEstado(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar/estado/${id}`, {});
  }
}
