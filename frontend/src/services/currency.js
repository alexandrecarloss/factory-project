import axios from 'axios';

const API_URL = 'https://economia.awesomeapi.com.br/last/USD-BRL';

export const CurrencyService = {
  async getUsdBrlRate() {
    try {
      const response = await axios.get(API_URL);
      return parseFloat(response.data.USDBRL.bid);
    } catch (error) {
      console.error("Erro ao buscar câmbio:", error);
      return 5.0;
    }
  }
};