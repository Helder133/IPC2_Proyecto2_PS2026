import { inject, Injectable } from '@angular/core';
import { HabilidadResponse } from '../../models/habilidad/HabilidadResponse';
import { Observable } from 'rxjs';
import { RestConstants } from '../../shared/restapi/rest-constants';
import { HttpClient } from '@angular/common/http';
import { HabilidadUpdate } from '../../models/habilidad/HabilidadUpdate';
import { HabilidadRequest } from '../../models/habilidad/HabilidadRequest';

@Injectable({
  providedIn: 'root',
})
export class HabilidadService {
  private http = inject(HttpClient);
  private apiUrl = `${new RestConstants().getApiUrl()}habilidad`;

  createHabilidad(req: HabilidadRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/create`, req);
  }

  getAllHabilidad(): Observable<HabilidadResponse[]> {
    return this.http.get<HabilidadResponse[]>(this.apiUrl);
  }

  getAllHabilidadActivada(): Observable<HabilidadResponse[]> {
    return this.http.get<HabilidadResponse[]>(`${this.apiUrl}/activa`);
  }

  getHabilidadByCoincidence(id: string): Observable<HabilidadResponse | HabilidadResponse[]> {
    return this.http.get<HabilidadResponse | HabilidadResponse[]>(`${this.apiUrl}/${id}`);
  }

  getHabilidadesDisponiblesFreelancer(): Observable<HabilidadResponse[]> {
    return this.http.get<HabilidadResponse[]>(`${this.apiUrl}/no-registrados/freelancer`);
  }

  updateHabilidad(req: HabilidadUpdate): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar`, req);
  }

  updateHabilidadEstado(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar/estado/${id}`, {});
  }
}
