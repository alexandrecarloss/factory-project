import { createApp } from "vue";
import { createI18n } from "vue-i18n";
import App from "./App.vue";
import "bootstrap/dist/css/bootstrap.min.css";

import en from "./locales/en.json";
import pt from "./locales/pt.json";

const i18n = createI18n({
  legacy: false,
  locale: "en",
  fallbackLocale: "en",
  messages: {
    en,
    pt,
  },
});

const app = createApp(App);
app.use(i18n);
app.mount("#app");
