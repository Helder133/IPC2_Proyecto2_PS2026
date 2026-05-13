import { inject, Injectable, signal } from '@angular/core';
import { UsuarioResponse } from '../../models/usuario/UsuarioResponse';
import { Observable } from 'rxjs';
import { UsuarioRequest } from '../../models/usuario/UsuarioRequest';
import { HttpClient } from '@angular/common/http';
import { RestConstants } from '../../shared/restapi/rest-constants';
import { FreelancerRequest } from '../../models/usuario/freelancer/FreelancerRequest';
import { ClienteRequest } from '../../models/usuario/client/ClienteRequest';
import { UsuarioUpdate } from '../../models/usuario/UsuarioUpdate';
import { FreelancerHabilidadRequest } from '../../models/usuario/freelancer/FreelancerHabilidadRequest';
import { CarteraRequest } from '../../models/usuario/cartera/CarteraRequest';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  private restConstants = new RestConstants();
  private http = inject(HttpClient);
  private apiUrl = `${this.restConstants.getApiUrl()}usuario`;

  createUsuarioClienteFreelancer(usuario: UsuarioRequest): Observable<UsuarioResponse> {
    return this.http.post<UsuarioResponse>(`${this.apiUrl}/cliente-freelancer`, usuario);
  }

  insertComplementoCliente(cliente: ClienteRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/complemento/cliente`, cliente);
  }

  insertComplementoFreelancer(freelancer: FreelancerRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/complemento/freelancer`, freelancer);
  }

  getAllUsuarios(): Observable<UsuarioResponse[]> {
    return this.http.get<UsuarioResponse[]>(this.apiUrl);
  }

  getUsuarioById(id: number): Observable<UsuarioResponse> {
    return this.http.get<UsuarioResponse>(`${this.apiUrl}/${id}`);
  }

  getUsuariosByCoincidence(coincidencia: string): Observable<UsuarioResponse[]> {
    return this.http.get<UsuarioResponse[]>(`${this.apiUrl}/coincidence/${coincidencia}`);
  }

  createUsuarioAdmin(usuario: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/admin`, usuario);
  }

  updateUsuarioEstado(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar/estado/${id}`, {});
  }

  updateUsuarioActualizar(usuarioUpdate: UsuarioUpdate): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar`, usuarioUpdate);
  }

  agregarHabilidad(req: FreelancerHabilidadRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/freelancer/habilidad`, req);
  }

  deleteFreelancerHabilidad(habilidadId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/freelancer/habilidad/${habilidadId}`);
  }

  carteraActual = signal<any>(null);
  refrescarCarteraGlobal() {
    this.http.get(`${this.apiUrl}/cartera`).subscribe({
      next: (res) => this.carteraActual.set(res),
      error: (err) => console.error('Error al cargar cartera', err)
    });
  }

  recargarCartera(req: CarteraRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/cartera/recarga`, req);
  }

  getTransacciones(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/cartera/transaccion`);
  }

}
