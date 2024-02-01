<template>
  <div id="map"></div>
</template>

<script lang="ts">
/*global google*/ //This line of code MUST be here, otherwise Eslint will have a stroke

import { defineComponent, watch, inject, ref, Ref } from "vue";
import IActivity from "../interfaces/Activity/IActivity.interface";
import ICoordinates from "@/interfaces/ICoordinates.interface";
import data from "@/../config.json";
import { Loader } from "@googlemaps/js-api-loader";
import { useStore } from "@/store";
import getActivityDifficultyNames from "../utils/getActivityDifficultyName";
import { useRouter } from "vue-router";

export default defineComponent({
    name: "Map",

    props: {
        center: { 
            type: Object as () => ICoordinates,
            required: false,
            },
        zoom: Number,
        mapTypeId: String,
        disableDefaultUI: Boolean,
        activityData: {
            type: Array,
            required: true,
        },
        getLocation: Boolean,
        setLocation: Boolean,
    },

  setup(props) {
    const store = useStore();
    const router = useRouter();
    const markers:Ref<google.maps.Marker[]> = ref([]);
    let map: google.maps.Map;
    let marker: google.maps.Marker; //This is used to place a marker when retriving coordinates from the map
    let currentSelectedMarker = -1; //Sat to a value an activity cannot have
    const choosenLocation = inject("coordinates", {
      lat: 0.0,
      lng: 0.0,
    } as ICoordinates);

    function initMap() : google.maps.Map {
      //If the Map component has a spesified center it will use it, if not, it will get a geolocation
      const newCenter: ICoordinates = props.center ? props.center : getUsersLocation();
      map = new google.maps.Map(document.getElementById("map") as HTMLElement, {
          center: newCenter,
          zoom: props.zoom || 14,
          mapTypeId: props.mapTypeId || "roadmap",
          disableDefaultUI: props.disableDefaultUI || true,
      });
      if (props.setLocation) {
        marker = new window.google.maps.Marker({
            position: map.getCenter()
          });
        marker.setMap(map);
      }
      if (props.getLocation) {
        //If the map will be used to retrive a postion based on a click
        map.addListener("click", (mapsMouseEvent: any) => {
          if (marker != null) {
            marker.setMap(null); //If the marker is already on the map; remove it
          }
          marker = new window.google.maps.Marker({
            position: mapsMouseEvent.latLng,
          });
          marker.setMap(map);
          choosenLocation.lat = mapsMouseEvent.latLng.lat();
          choosenLocation.lng = mapsMouseEvent.latLng.lng();
        });
      }
      return map;
    }

    function getUsersLocation(): ICoordinates {
      let cacheLocation = store.getters.userLocation; //Gets the cached location from the store
      if (cacheLocation.lat) { //If we have a cached value the '.lat' will return a value giving us true and we can safely return the value    
          return cacheLocation;
      }
      let pos: ICoordinates = { lat: 0.0, lng: 0.0 } as ICoordinates;
      if (navigator.geolocation) {
          //eslint-disable-next-line
          navigator.geolocation.getCurrentPosition((position: GeolocationPosition) => { //Gets the geolocation from the browser
              pos = {
                  lat: position.coords.latitude,
                  lng: position.coords.longitude,
              } as ICoordinates;
              map.setCenter(pos); //Just sets the maps center when we find a new value
              store.dispatch("updateUserLocation", pos);
          });
      }
      //Browser doesn't support geolcation
      return pos;
    };

    watch(() => props.center, (newValue, oldValue) => {
      if (newValue != oldValue && props.setLocation) {
        map.setCenter(newValue as ICoordinates);
        marker.setPosition(map.getCenter());
      }
    });

    watch(() => props.activityData, (newValue, oldValue) => {
      if (newValue != oldValue) {
        markers.value.forEach(element => {
          element.setVisible(false);
        });

        markers.value = [];
        const infoWindow = new window.google.maps.InfoWindow();

        props.activityData.forEach((element) => {
          const activity = element as IActivity;
          let marker = new window.google.maps.Marker({
            position: { lat: activity.latitude, lng: activity.longitude },
          });

          marker.setMap(map);
          markers.value.push(marker);
          marker.setTitle(activity.description + " | " + activity.activityId);
          const contentString =
          '<div id="activity">' +
          "<div><h3>" +
          activity.description +
          "</h3><h4>" +
          activity.startTime +
          " | " +
          activity.place +
          ", " +
          activity.city +
          "</h4></div><div><h4>" +
          getActivityDifficultyNames(activity.difficulty || 0) +
          "</h4></div></div>";

          marker.addListener("click", () => {
            infoWindow.close();
            infoWindow.setContent(contentString);
            infoWindow.open(marker.getMap(), marker);
            if (activity.activityId == currentSelectedMarker) {
              router.push("/activity/" + activity.activityId);
            }
            currentSelectedMarker = activity.activityId;
          });
        });
      }
    });

    const loader = new Loader({
      apiKey: data.googleAPIKey,
      version: "weekly",
    });

    loader.load().then(() => {
      if (map == null) {
        map = initMap();
      }
    });

    return {
      choosenLocation,
    };
  },
});
</script>

<style scoped>
#map {
  height: 100%;
}

html,
body {
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
}
</style>
