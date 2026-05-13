import { EnumUsuario } from "./EnumUsuario";

export interface UsuarioRequest {
    nombreCompleto: string;
    userName: string;
    password?: string;
    email: string;
    telefono: string;
    direccion: string;
    cui: string;
    fechaNacimiento: string; // Formato YYYY-MM-DD
    rol: EnumUsuario;
}