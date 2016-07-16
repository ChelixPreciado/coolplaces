package interware.coolapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chelixpreciado on 7/15/16.
 */
public class Place implements Parcelable{

    private int id;
    private String lugar;
    private String estado;
    private Long lat;
    private Long lon;
    private String imagen;
    private String descripcion;

    public Place(){}

    protected Place(Parcel in) {
        id = in.readInt();
        lugar = in.readString();
        estado = in.readString();
        imagen = in.readString();
        descripcion = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getLat() {
        return lat;
    }

    public void setLat(Long lat) {
        this.lat = lat;
    }

    public Long getLon() {
        return lon;
    }

    public void setLon(Long lon) {
        this.lon = lon;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(lugar);
        parcel.writeString(estado);
        parcel.writeString(imagen);
        parcel.writeString(descripcion);
    }
}
