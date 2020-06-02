export interface Period {
    name: string;
    id: number;
    fechaPago: Date;
    cuota: number;
    principal: number;
    interest: number;
    restante: number;
    capitalPendiente: number;
}
