import { createApp } from "vue";
import { createI18n } from "vue-i18n";
import App from "./App.vue";
import "./style.css";
import en from "./locales/en.json";
import pt from "./locales/pt.json";

const numberFormats = {
  'en': {
    currency: {
      style: 'currency',
      currency: 'USD',
      notation: 'standard'
    }
  },
  'pt': {
    currency: {
      style: 'currency',
      currency: 'BRL',
      notation: 'standard'
    }
  }
}

const i18n = createI18n({
  legacy: false,
  locale: "en",
  fallbackLocale: "en",
  messages: {
    en,
    pt,
  },
  numberFormats,
});

const app = createApp(App);
app.use(i18n);
app.mount("#app");
