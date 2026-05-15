import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { RestConstants } from '../../shared/restapi/rest-constants';
import { Observable } from 'rxjs';
import { EntregaRequest } from '../../models/entrega/EntregaRequest';
import { EntregaResponse } from '../../models/entrega/EntregaResponse';
import { EntregaUpdate } from '../../models/entrega/EntregaUpdate';
import { EntregaRechazada } from '../../models/entrega/EntregaRechazada';
import { ContratoFinalizado } from '../../models/entrega/ContratoFinalizado';

@Injectable({
  providedIn: 'root',
})
export class EntregaService {
  private http = inject(HttpClient);
  private apiUrl = `${new RestConstants().getApiUrl()}entrega`;

  createEntrega(req: EntregaRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/create`, req);
  }

  getEntregaById(id: number): Observable<EntregaResponse> {
    return this.http.get<EntregaResponse>(`${this.apiUrl}/${id}`);
  }

  getAllEntregasOfAContract(contratoId: number): Observable<EntregaResponse[]> {
    return this.http.get<EntregaResponse[]>(`${this.apiUrl}/contrato/${contratoId}`);
  }

  updateEntrega(req: EntregaUpdate): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar`, req);
  }

  rechazarEntrega(req: EntregaRechazada): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar/rechazar`, req);
  }

  aprobarEntrega(entregaId: number, req: ContratoFinalizado): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar/aprobar/${entregaId}`, req);
  }
}
