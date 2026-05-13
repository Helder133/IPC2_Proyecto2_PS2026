import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { RestConstants } from '../../shared/restapi/rest-constants';
import { Observable } from 'rxjs';
import { PropuestaRequest } from '../../models/propuesta/PropuestaRequest';
import { PropuestaResponse } from '../../models/propuesta/PropuestaResponse';
import { PropuestaDetalleResponse } from '../../models/propuesta/PropuestaDetalleResponse';
import { PropuestaUpdate } from '../../models/propuesta/PropuestaUpdate';

@Injectable({
  providedIn: 'root',
})
export class PropuestaService {
  private http = inject(HttpClient);
  private apiUrl = `${new RestConstants().getApiUrl()}propuesta`;

  createPropuesta(req: PropuestaRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/create`, req);
  }

  getPropuestaById(id: number): Observable<PropuestaResponse> {
    return this.http.get<PropuestaResponse>(`${this.apiUrl}/${id}`);
  }

  getAllPropuestaFromAFreelancer(proyectoId: number): Observable<PropuestaResponse[]> {
    return this.http.get<PropuestaResponse[]>(`${this.apiUrl}/freelancer/${proyectoId}`);
  }

  getAllPropuestaForAProyecto(proyectoId: number): Observable<PropuestaDetalleResponse[]> {
    return this.http.get<PropuestaDetalleResponse[]>(`${this.apiUrl}/cliente/${proyectoId}`);
  }

  updatePropuesta(req: PropuestaUpdate): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar`, req);
  }

  retirarPropuesta(propuestaId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/retirar/${propuestaId}`, {});
  }

  getHistorialPropuestas(estado: string): Observable<any[]> {
    if(!estado){
      estado = 'TODAS';
    }
    return this.http.get<any[]>(`${this.apiUrl}/usuario/historial/${estado}`);
  }

  aceptarPropuesta(propuestaId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/aceptar/${propuestaId}`, {});
  }

  rechazarPropuesta(propuestaId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/rechazar/${propuestaId}`, {});
  }
}
