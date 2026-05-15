import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { RestConstants } from '../../shared/restapi/rest-constants';
import { Nueva_h_cRequest } from '../../models/nueva_h_c/Nueva_h_cRequest';
import { Observable } from 'rxjs';
import { Nueva_h_cResponse } from '../../models/nueva_h_c/Nueva_h_cResponse';
import { Nueva_h_cUpdate } from '../../models/nueva_h_c/Nueva_h_cUpdate';

@Injectable({
  providedIn: 'root',
})
export class SolicitudService {
  private http = inject(HttpClient);
  private apiUrl = `${new RestConstants().getApiUrl()}nueva-h-c`;

  createSolicitud(req: Nueva_h_cRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/create`, req);
  }

  getMisSolicitudes(): Observable<Nueva_h_cResponse[]> {
    // Llama al endpoint getAllNueva_h_cByUsuarioId
    return this.http.get<Nueva_h_cResponse[]>(`${this.apiUrl}`);
  }

  updateSolicitud(req: Nueva_h_cUpdate): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar`, req);
  }

  getAllAdmin(): Observable<Nueva_h_cResponse[]> {
    return this.http.get<Nueva_h_cResponse[]>(`${this.apiUrl}/admin`);
  }

  aceptarSolicitud(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/aceptar/${id}`, {});
  }

  rechazarSolicitud(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/rechazar/${id}`, {});
  }
}
