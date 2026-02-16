// Utilidades para manejo de monedas y conversión.
// Base: EUR (1.0)

export const CURRENCIES = {
    EUR: { symbol: '€', label: 'Euros (EUR)', rate: 1.0 },
    USD: { symbol: '$', label: 'Dólares (USD)', rate: 1.10 },
    MXN: { symbol: '$', label: 'Pesos Mexicanos (MXN)', rate: 18.50 },
    NPR: { symbol: '₨', label: 'Rupias Nepalíes (NPR)', rate: 145.00 }
};

// Formatea un monto basado en la moneda seleccionada.
// @param {number} amount Monto en EUR
// @param {string} currencyCode Código de la moneda (EUR, USD, etc.)
// @returns {string} Monto formateado con símbolo y tasa aplicada
export const formatCurrency = (amount, currencyCode = 'EUR') => {
    const currency = CURRENCIES[currencyCode] || CURRENCIES.EUR;
    const convertedAmount = amount * currency.rate;

    // Usar Intl.NumberFormat para un formato profesional
    const formatter = new Intl.NumberFormat('es-ES', {
        style: 'currency',
        currency: currencyCode,
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });

    // Si es NPR, el símbolo a veces falla en navegadores viejos, forzamos símbolo si es necesario
    // Pero Intl.NumberFormat es lo ideal.
    return formatter.format(convertedAmount);
};

// Retorna solo el símbolo de la moneda.
export const getCurrencySymbol = (currencyCode) => {
    return CURRENCIES[currencyCode]?.symbol || '€';
};
