<template>
  <div class="container">
    <img id="img" :src="iconPath" alt="Ingen værdata" />
    <p>{{ temperature }}</p>
  </div>
</template>

<script lang="ts">
import notConfigAxios from "axios";
import { defineComponent, onMounted, ref, Ref } from "vue";

export default defineComponent({
  name: "Weather",
  props: {
    latitude: {
      type: Number,
      required: true,
    },
    longitute: {
      type: Number,
      required: true,
    },
    time: {
      type: String,
      required: true,
    },
  },
  setup(props) {
    const temperature: Ref<string> = ref("");
    const iconPath: Ref<string> = ref("");
    onMounted(async () => {
      if (
        props.latitude !== null &&
        props.longitute !== null &&
        props.time !== null
      ) {
        try {
          const response = await notConfigAxios.get(
            "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=" +
              Math.round((props.latitude + Number.EPSILON) * 10000) / 10000 +
              "&lon=" +
              Math.round((props.longitute + Number.EPSILON) * 10000) / 10000
          );

          let myHour = props.time.split(" ")[1].split(":")[0];
          let i = 0;
          for (let s in response.data["properties"]["timeseries"]) {
            let timeStamp =
              response.data["properties"]["timeseries"][s]["time"];
            if (
              timeStamp.split("T")[1].split(":")[0] === myHour &&
              timeStamp.split("T")[0] === props.time.split(" ")[0]
            ) {
              break;
            }
            i++;
          }

          if (i < response.data["properties"]["timeseries"].length) {
            temperature.value =
              Math.round(
                response.data["properties"]["timeseries"][i]["data"]["instant"][
                  "details"
                ]["air_temperature"]
              ).toString() + "°C";
            let symbol =
              response.data["properties"]["timeseries"][i]["data"][
                "next_1_hours"
              ]["summary"]["symbol_code"];
            iconPath.value = require("../assets/weatherIcons/" +
              symbol +
              ".png");
          }
        } catch (error) {
          //Can't send to error page, to avoid error
          //when there is no weather data in the api
        }
      }
    });

    return {
      temperature,
      iconPath,
    };
  },
});
</script>

<style scoped>
.container {
  display: grid;
  align-content: center;
  width: 10%;
  grid-template-columns: 1fr 1fr;
}

p {
  display: inline;
  font-size: 1rem;
  line-height: 5px;
}

#img {
  width: 40px;
  height: 40px;
  display: block;
  margin-right: 10px;
}
</style>
