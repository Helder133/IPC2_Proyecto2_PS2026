import { inject, Injectable } from '@angular/core';
import { ProyectoResponse } from '../../models/proyecto/ProyectoResponse';
import { Observable } from 'rxjs';
import { ProyectoRequest } from '../../models/proyecto/ProyectoRequest';
import { HttpClient } from '@angular/common/http';
import { RestConstants } from '../../shared/restapi/rest-constants';
import { ProyectoUpdate } from '../../models/proyecto/ProyectoUpdate';

@Injectable({
  providedIn: 'root',
})
export class ProyectoService {
  private http = inject(HttpClient);
  private apiUrl = `${new RestConstants().getApiUrl()}proyecto`;

  createProyecto(req: ProyectoRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/create`, req);
  }

  getMisProyectos(): Observable<ProyectoResponse[]> {
    return this.http.get<ProyectoResponse[]>(`${this.apiUrl}/usuario`);
  }

  getMisProyectosByCoincidencia(titulo: string): Observable<ProyectoResponse[]> {
    return this.http.get<ProyectoResponse[]>(`${this.apiUrl}/usuario/${titulo}`);
  }
  updateProyecto(req: ProyectoUpdate): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar`, req);
  }

  cancelarProyecto(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar/cancelar/${id}`, {});
  }

  agregarHabilidadProyecto(proyectoId: number, req: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/${proyectoId}/habilidad`, req);
  }

  eliminarHabilidadProyecto(proyectoId: number, habilidadId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/eliminar/${proyectoId}/habilidad/${habilidadId}`);
  }

  getUsuarioProyectoById(proyectoId: number): Observable<ProyectoResponse> {
    return this.http.get<ProyectoResponse>(`${this.apiUrl}/${proyectoId}/usuario`);
  }

  getAllProyectosAbiertos(): Observable<ProyectoResponse[]> {
    return this.http.get<ProyectoResponse[]>(`${this.apiUrl}`);
  }

  getProyectosByCategoria(categoriaId: number): Observable<ProyectoResponse[]> {
    return this.http.get<ProyectoResponse[]>(`${this.apiUrl}/categoria/${categoriaId}`);
  }

  getProyectosByHabilidad(habilidadId: number): Observable<ProyectoResponse[]> {
    return this.http.get<ProyectoResponse[]>(`${this.apiUrl}/habilidad/${habilidadId}`);
  }

  getProyectosByPresupuesto(inicio: number, fin: number): Observable<ProyectoResponse[]> {
    return this.http.get<ProyectoResponse[]>(`${this.apiUrl}/presupuesto/${inicio}/${fin}`);
  }

  getProyectoById(id: number): Observable<ProyectoResponse> {
    return this.http.get<ProyectoResponse>(`${this.apiUrl}/${id}`);
  }

  marcarProyectoEnRevision(proyectoId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar/en-reserva/${proyectoId}`, {});
  }

}
