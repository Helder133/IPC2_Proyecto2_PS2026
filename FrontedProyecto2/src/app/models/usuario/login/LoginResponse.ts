import { UsuarioResponse } from "../UsuarioResponse";

export interface LoginResponse {
    token: string;
    usuario: UsuarioResponse;
}