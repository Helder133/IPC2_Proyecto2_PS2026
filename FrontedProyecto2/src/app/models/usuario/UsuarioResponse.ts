import { ClienteResponse } from "./client/ClienteResponse";
import { FreelancerResponse } from "./freelancer/FreelancerResponse";
import { EnumUsuario } from "./EnumUsuario";

export interface UsuarioResponse {
    usuarioId: number;
    nombreCompleto: string;
    userName: string;
    email: string;
    telefono: string;
    direccion: string;
    cui: string;
    fechaNacimiento: string;
    rol: EnumUsuario;
    estado: boolean;
    cliente?: ClienteResponse;
    freelancer?: FreelancerResponse;
}