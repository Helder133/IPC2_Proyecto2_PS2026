import { inject, Injectable } from '@angular/core';
import { ConfiguracionSistemaResponse } from '../../models/configuracionSistema/ConfiguracionSistemaResponse';
import { Observable } from 'rxjs';
import { ConfiguracionSistemaRequest } from '../../models/configuracionSistema/ConfiguracionSistemaRequest';
import { RestConstants } from '../../shared/restapi/rest-constants';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ConfiguracionService {
  private http = inject(HttpClient);
  private apiUrl = `${new RestConstants().getApiUrl()}configuracion-sistema`;

  create(config: ConfiguracionSistemaRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/create`, config);
  }

  getUltimo(): Observable<ConfiguracionSistemaResponse> {
    return this.http.get<ConfiguracionSistemaResponse>(`${this.apiUrl}/ultimo`);
  }

  getAll(): Observable<ConfiguracionSistemaResponse[]> {
    return this.http.get<ConfiguracionSistemaResponse[]>(this.apiUrl);
  }
}
