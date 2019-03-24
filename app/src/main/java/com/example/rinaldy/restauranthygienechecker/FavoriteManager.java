package com.example.rinaldy.restauranthygienechecker;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class FavoriteManager {
    private static final String FAVORITE_KEY = "favorites";

    private Context mContext;

    public FavoriteManager(Context context) {
        mContext = context;
    }

    public void addToFavorites(Establishment establishment) {
        if (establishment != null && establishment.getFHRSID() != null) {
            SharedPreferences sp = mContext.getSharedPreferences("favorites", Context.MODE_PRIVATE);
            String favoritesStr = sp.getString(FAVORITE_KEY, null);
            ArrayList<Establishment> list = null;
            if (!TextUtils.isEmpty(favoritesStr)) {
                list = new Gson().fromJson(favoritesStr, new TypeToken<List<Establishment>>() {
                }.getType());
            } else {
                list = new ArrayList<>();
            }

            if (list != null && list.size() > 0) {
                for (Establishment item : list) {
                    if (establishment.getFHRSID().equals(item.getFHRSID())) {
                        //establishment already added, avoid to add duplicated one
                        return;
                    }
                }
            }
            list.add(establishment);
            Gson gson = new Gson();
            String favoritesJson = gson.toJson(list);
            sp.edit().putString(FAVORITE_KEY, favoritesJson).commit();
        }
    }

    public void removeFromFavorites(Establishment establishment) {
        if (establishment != null && establishment.getFHRSID() != null) {
            SharedPreferences sp = mContext.getSharedPreferences("favorites", Context.MODE_PRIVATE);
            String favoritesStr = sp.getString(FAVORITE_KEY, null);

            if (!TextUtils.isEmpty(favoritesStr)) {
                ArrayList<Establishment> list = new Gson().fromJson(favoritesStr, new TypeToken<List<Establishment>>() {
                }.getType());

                if (list != null && list.size() > 0) {
                    Establishment itemToRemove = null;
                    for (Establishment item : list) {
                        if (establishment.getFHRSID().equals(item.getFHRSID())) {
                            itemToRemove = item;
                            break;
                        }
                    }

                    list.remove(itemToRemove);
                    Gson gson = new Gson();
                    String favoritesJson = gson.toJson(list);
                    sp.edit().putString(FAVORITE_KEY, favoritesJson).commit();
                }

            }
        }
    }

    public ArrayList<Establishment> getFavorites() {
            SharedPreferences sp = mContext.getSharedPreferences("favorites", Context.MODE_PRIVATE);
            String favoritesStr = sp.getString(FAVORITE_KEY, null);
            if (!TextUtils.isEmpty(favoritesStr)) {
                if (!TextUtils.isEmpty(favoritesStr)) {
                    try {
                        return new Gson().fromJson(favoritesStr, new TypeToken<ArrayList<Establishment>>() {
                        }.getType());
                    }catch (Exception e) {
                        return null;
                    }
                }
            }
            return null;
    }

    public boolean isEstablishmentInFavorites(Integer establishmentId) {

        if (establishmentId != null) {
            List<Establishment> favoirteList = getFavorites();
            if (favoirteList != null && favoirteList.size() > 0) {
                for (Establishment item : favoirteList) {
                    if (item.getFHRSID().equals(establishmentId)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
