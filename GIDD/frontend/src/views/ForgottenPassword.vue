<template>
  <div id="forgotten-password">
    <!--Add TheHeader component -->
    <h1>Tilbakestill</h1>
    <h1>passord</h1>
    <p>
      Vennligst skriv inn e-postadressen du registrerte deg med, så vil du motta
      en e-post med informasjon om gjennoppretting av passord
    </p>
    <input v-model="email" type="email" placeholder="E-post" id="email" />
    <div class="send-btn">
      <button
        @click="sendMailForgotPassword"
        alt="Knapp for å sende e-post når passord er glemt"
      >
        Send
      </button>
    </div>
    <p id="feedback" v-if="emailIsNotEmpty">{{ makeEmailFeedback }}</p>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref } from "vue";
import { useRouter } from "vue-router";

export default defineComponent({
  name: "ForgottenPassword",
  setup() {
    const router = useRouter();
    const email = ref("");
    const emailFeedback = ref("");

    const emailIsNotEmpty = computed(() => {
      return email.value.trim() !== "";
    });

    const sendMailForgotPassword = (): void => {
      if (validEmail.value && emailIsNotEmpty.value) {
        router.push("/log-in");
      }
    };

    const makeEmailFeedback = computed(() => {
      if (!validEmail.value) {
        return emailFeedback.value + "E-postadressen må være gyldig";
      }
      return emailFeedback.value + "";
    });

    const validEmail = computed(() => {
      return email.value.includes("@") && email.value.includes(".");
    });

    return {
      router,
      email,
      emailIsNotEmpty,
      makeEmailFeedback,
      sendMailForgotPassword,
      emailFeedback,
    };
  },
});
</script>

<style scoped lang="scss">
#forgotten-password {
  width: 340px;
  margin: 0 auto;
  box-sizing: border-box;
  padding-top: 90px;
}

h1 {
  line-height: 0.5em;
}

p {
  margin: 25px;
  text-align: left;
}

button {
  margin: 1.5em 0 1em 0;
  align-content: center;
  text-transform: uppercase;
  font-weight: 600;
  letter-spacing: 1px;
}

input {
  margin: 1em 0 0 0;
}
</style>
