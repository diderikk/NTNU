<template>
  <div id="home">
    <div id="home-intro">
      <div id="home-intro-content">
        <h4>Gidd å bli kjent med nye folk</h4>
      </div>
    </div>

    <div id="info">
      <div id="info-read-more">
        <div id="info-read-more-signup">
          <router-link class="router-link-button" to="/sign-up"
            >Opprett bruker</router-link
          >
        </div>
        <div id="info-read-more-login">
          Har du allerede bruker?
          <router-link id="log-in" to="/log-in">Logg inn</router-link>
        </div>
        <div @click="scrollToReadMore" id="info-read-more-scroll">
          Les mer
          <i class="fa fa-arrow-right" aria-hidden="true"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { useRouter } from "vue-router";

export default defineComponent({
  name: "Home",
  components: {},
  setup() {
    const router = useRouter();
    const scrollToReadMore = ref(() => {
      router.push("/read-more");
    });

    return {
      scrollToReadMore,
    };
  },
});
</script>

<style lang="scss" scoped>
@import url("https://fonts.googleapis.com/css2?family=Tajawal:wght@700&display=swap");
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css");

$primary-color: #282828;
$secondary-color: #ea4b4b;

#home {
  height: 100%;
  background: url("../../img/background.jpg");
  background-position: 40% 50%;
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
  margin-top: -84px;
  bottom: 0px;
  z-index: -1;
  overflow: hidden;
  @media only screen and (min-width: 600px) {
    background-position: 50% 60%;
  }
}

#home-intro {
  width: 100%;
  @media only screen and (min-width: 600px) {
    width: 50%;
  }
}

#home-intro-content {
  text-align: center;
  width: 50%;
  margin: auto;
  padding-top: 100px;
}

h4 {
  font-family: "Tajawal", sans-serif;
  font-size: 2.3rem;
  color: #282828;
  letter-spacing: 0cm;
  line-height: 2.1rem;
}

#info {
  width: 100%;
  @media only screen and (min-width: 600px) {
    width: 50%;
    bottom: 100px;
  }
}

#info-read-more {
  width: 100%;
  margin: auto;
  @media only screen and (min-width: 600px) {
    padding: 1.3rem;
    width: 50%;
  }
}

#info-read-more-signup {
  padding: 1rem;
}

#info-read-more-login {
  padding: 0.5rem;
  font-size: 0.833rem;
}

#info-read-more-scroll {
  color: $secondary-color;
  padding: 1.2rem;
}

#log-in {
  color: $secondary-color;
  text-shadow: 0 0 7px #ffffff;
  font-weight: 700;
}
</style>
