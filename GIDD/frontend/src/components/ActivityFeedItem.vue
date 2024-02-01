<template>
  <div id="activity" @click="activityClicked">
    <div id="map">
      <img
        id="feed-img"
        v-if="activityHasImage"
        :src="getImagesFromDb"
        alt="Aktivitetsbilde for å vise på framsiden for aktiviteter"
      />
      <img
        v-else
        id="map-img"
        :src="
          'https://maps.googleapis.com/maps/api/staticmap?center=' +
          activityData.latitude +
          ',' +
          activityData.longitude +
          '&zoom=14&size=600x350&markers=color:blue%7Clabel:A%7C' +
          activityData.latitude +
          ',' +
          activityData.longitude +
          '&key=' +
          apiKey
        "
        alt="Kart vises av plassering hvis aktivitet ikke har bilde"
      />
    </div>
    <div>
      <h3>{{ activityData.title }}</h3>
      <p>{{ dateTimeFormatter }}</p>
      <p>{{ location }}</p>
      <p>{{ difficulty }}</p>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref } from "vue";
import { useRouter } from "vue-router";
import IActivity from "../interfaces/Activity/IActivity.interface";
import data from "@/../config.json";
import getActivityDifficultyName from "../utils/getActivityDifficultyName";

export default defineComponent({
  name: "ActivityPreviewFeed",

  props: {
    activityData: {
      type: Object as () => IActivity,
      required: true,
    },
  },

  setup(props) {
    const router = useRouter();
    const location = computed((): string => {
      return props.activityData.place + ", " + props.activityData.city;
    });

    const apiKey = data.googleAPIKey;

    const difficulty = computed(() => {
      return getActivityDifficultyName(props.activityData.difficulty || 0);
    });

    const activityClicked = (): void => {
      router.push("/activity/" + props.activityData.activityId);
    };

    const getImagesFromDb = computed(() => {
      return props.activityData.activityPicture;
    });

    const activityHasImage = computed(() => {
      return (
        props.activityData.activityPicture !== "" &&
        props.activityData.activityPicture !== null
      );
    });

    /**
     * Formats the date and time
     */
    const dateTimeFormatter = computed(() => {
      const temp = props.activityData.startTime.split(" ");
      const dateArray = temp[0].split("-");
      const date = ref(dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0]);
      const time = ref(temp[1]);
      return date.value + " kl. " + time.value;
    });

    return {
      difficulty,
      location,
      activityClicked,
      apiKey,
      activityHasImage,
      getImagesFromDb,
      dateTimeFormatter,
    };
  },
});
</script>

<style scoped lang="scss">
#activity {
  cursor: pointer;
  text-align: left;
  width: 100%;
}

#activity:hover {
  border-radius: 20px;
  background-color: #f0f0f0;
}

h3 {
  margin: 6px;
}

p {
  padding-left: 6px;
  margin: 0px;
}

#map {
  width: 100%;
  margin: 15px 0px 15px 0px;
}

#map-img,
#feed-img {
  border-radius: 20px;
  width: 100%;
  object-fit: cover;
  height: 200px;
  object-position: center;
  @media only screen and (min-width: 600px) {
    width: 100%;
    height: 300px;
  }
}
</style>
