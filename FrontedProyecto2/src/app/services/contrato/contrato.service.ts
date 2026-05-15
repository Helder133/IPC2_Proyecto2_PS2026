import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { RestConstants } from '../../shared/restapi/rest-constants';
import { ContratoResponse } from '../../models/contrato/ContratoResponse';
import { Observable } from 'rxjs';
import { ContratoCancelado } from '../../models/contrato/ContratoCancelado';

@Injectable({
  providedIn: 'root',
})
export class ContratoService {
  private http = inject(HttpClient);
  private apiUrl = `${new RestConstants().getApiUrl()}contrato`;

  getAllContractsFromAFreelancer(): Observable<ContratoResponse[]> {
    return this.http.get<ContratoResponse[]>(`${this.apiUrl}/freelancer`);
  }

  getAllContractsFromACliente(): Observable<ContratoResponse[]> {
    return this.http.get<ContratoResponse[]>(`${this.apiUrl}/cliente`);
  }

  getContratoById(id: number): Observable<ContratoResponse> {
    return this.http.get<ContratoResponse>(`${this.apiUrl}/${id}`);
  }

  getContratoByPropuestaId(propuestaId: number): Observable<ContratoResponse> {
    return this.http.get<ContratoResponse>(`${this.apiUrl}/propuesta/${propuestaId}`);
  }

  cancelarContrato(req: ContratoCancelado): Observable<any> {
    return this.http.put(`${this.apiUrl}/cancelar`, req);
  }
}
