


package com.example.countries;

import io.reactivex.Single;


    public class CountryService {


        public Single<String> codes() {
            return getCodes();
        }

        private Single<String> getCodes() {
            RestClient restClient = new RestClient();
            return Single.just(restClient.getCodes());
        }

        public Single<String> capital(String code) {

            return Single.create(singleSubscriber -> {
                RestClient restClient = new RestClient();
                singleSubscriber.onSuccess(restClient.capital(code));
            });

        }

    }