import { ClienteUpdate } from "./client/ClienteUpdate";
import { FreelancerUpdate } from "./freelancer/FreelancerUpdate";

export interface UsuarioUpdate {
    usuarioId: number;
    nombreCompleto: string;
    userName: string;
    password?: string;
    email: string;
    telefono: string;
    direccion: string;
    cui: string;
    fechaNacimiento: string; 
    clienteUpdate?: ClienteUpdate | null;
    freelancerUpdate?: FreelancerUpdate | null;
}