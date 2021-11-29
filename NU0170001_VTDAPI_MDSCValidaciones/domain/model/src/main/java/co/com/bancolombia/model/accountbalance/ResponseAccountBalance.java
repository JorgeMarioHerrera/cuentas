package co.com.bancolombia.model.accountbalance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAccountBalance {
    /**
     * Corresponde al saldo que el cliente puede utilizar; Es el saldo efectivo menos bloqueos, menos bolsillos,
     * sin cupo de sobregiro y sin canje.
     */
    private float available;

    /**
     * Es el saldo líquido que tiene la cuenta, es decir,  el disponible, mas bloqueos, más bolsillos. Sin cupos
     * y sin canje.
     */
    private float effective;

    /**
     * Es el saldo total de la cuenta, es decir, el saldo efectivo más el canje. No tiene cupos.
     */
    private float current;

    /**
     * Es el saldo que el cliente puede utilizar incluyendo el valor del cupo de sobregiro.
     */
    private float availableOverdraftBalance;

    /**
     * Valor total del Cupo de Sobregiro que se otorgó a la cuenta.
     */
    private float overdraftValue;

    /**
     * Valor del Cupo de Sobregiro que tiene disponible para utilizar.
     */
    private float availableOverdraftQuota;

    /**
     * Saldo depositado en la cuenta en cheque, el cual aún no ha finalizado el proceso de canje y no puede ser
     * utilizado por el cliente.
     */
    private float clearing;

    /**
     * Es el valor que está bloqueado en la cuenta porque ésta tiene una deuda pendiente con el banco.
     */
    private float receivable;

    /**
     * es el saldo que está bloqueado en la cuenta correspondiente a embargos, adelanto de ingresos, master debit y
     * mínimo a mantener.
     */
    private float blocked;

    /**
     * Información del saldo en canje de la cuenta al inicio de día. El saldo en canje corresponde a los depósitos
     * realizados en la cuenta con cheque y que no habían finalizado el proceso de canje.
     */
    private float clearingStartDay;

    /**
     * Información del saldo disponible al inicio del día. El saldo disponible corresponde al saldo que el cliente
     * puede utilizar; Es el saldo efectivo menos bloqueos, menos bolsillos, sin cupo de sobregiro y sin canje.
     */
    private float availableStartDay;

    /**
     * Información del saldo total al inicio de día. Es el saldo total de la cuenta corresponde al saldo efectivo
     * más el canje. No tiene cupos."
     */
    private float currentStartDay;

    /**
     * Información del saldo efectivo al inicio de día. El saldo efectivo corresponde al saldo líquido que tiene
     * la cuenta, es decir,  el disponible, mas bloqueos, más bolsillos. Sin cupos y sin canje.
     */
    private float effectiveStartDay;

    /**
     * Es la sumatoria de los saldos de los bolsillos de la cuenta.
     */
    private float pockets;

    /**
     * Cupo remesas consignadas.
     */
    private float remittanceQuota;

    /**
     * El cupo de remesas es un cupo de crédito que se consume con base en las remesas consignadas y pendientes
     * que tenga el cliente. El cupo rota según las confirmaciones efectuadas, y este cupo lo autoriza el gerente
     * de la relación con un monto determinado. Este cupo permite que el cliente disponga del dinero el mismo día
     * de la consignación del cheque remesa y de esta forma no tenga que esperar que el banco reciba el cheque
     * a conformidad (hasta 13 días hábiles).
     */
    private float agreedRemittanceQuota;

    /**
     * Es la porción del cupo de remesa que el cliente ya ha utilizado, es decir, la cuenta recibió una consignación
     * con cheque remesa y esta fue autorizada para que el cliente disponga del dinero el mismo día de la consignación.
     */
    private float remittanceQuotaUsage;

    /**
     * Intereses de sobregiro causados en la cuenta que tienen menos de 90 días.
     */
    private float normalInterest;

    /**
     * Intereses de sobregiro que tienen más de 90 días de haber sido causados. Pueden existir intereses en suspensión
     * con menos de 90 días dependiendo de la calificación del cliente.
     */
    private float suspensionInterest;

    private float clearingQuota;
}
