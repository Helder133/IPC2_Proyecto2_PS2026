import { EnumTransaccion } from "./EnumTransaccion";

export interface TransaccionResponse {
    transaccionId: number;
    usuarioId: number;
    tipo: EnumTransaccion;
    monto: number;
    fecha: string;
}