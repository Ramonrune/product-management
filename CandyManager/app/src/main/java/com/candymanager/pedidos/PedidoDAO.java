package com.candymanager.pedidos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.candymanager.db.PedidoAjudante;
import com.candymanager.db.PedidoContrato;
import com.candymanager.db.ProdutoContrato;
import com.candymanager.login.LoginSharedPreferences;
import com.candymanager.produtos.ProdutoModel;
import com.candymanager.util.UUIDGenerator;

import java.util.ArrayList;

public class PedidoDAO {

    private PedidoAjudante pedidoAjudante;

    private Context contexto;

    public void setContext(Context contexto){
        this.contexto = contexto;
    }

    public Context getContext(){
        return this.contexto;
    }

    public PedidoDAO(Context context){
        pedidoAjudante = new PedidoAjudante(context);
        this.contexto = context;
    }

    public boolean cadastrar(PedidoModel model){

        SQLiteDatabase db = pedidoAjudante.getWritableDatabase();

        ContentValues values = new ContentValues();

        LoginSharedPreferences loginSharedPreferences = new LoginSharedPreferences(contexto);

        String uuid = UUIDGenerator.uuid();

        values.put(PedidoContrato.PedidoEntrada.COLUNA_ID_PEDIDO, uuid);
        values.put(PedidoContrato.PedidoEntrada.COLUNA_BAIRRO, model.getBairro());
        values.put(PedidoContrato.PedidoEntrada.COLUNA_CEP, model.getCep());
        values.put(PedidoContrato.PedidoEntrada.COLUNA_ENDERECO, model.getEndereco());
        values.put(PedidoContrato.PedidoEntrada.COLUNA_NUMERO, model.getNumero());
        values.put(PedidoContrato.PedidoEntrada.COLUNA_DATA, model.getData());
        values.put(PedidoContrato.PedidoEntrada.COLUNA_ID_USUARIO, loginSharedPreferences.getId());
        long newRowId = db.insert(ProdutoContrato.ProdutoEntrada.NOME_TABELA, null, values);

        if(newRowId == -1){
            return false;
        }

        return true;

    }

    public boolean alterar(PedidoModel model){
        LoginSharedPreferences loginSharedPreferences = new LoginSharedPreferences(contexto);

        SQLiteDatabase db = pedidoAjudante.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PedidoContrato.PedidoEntrada.COLUNA_BAIRRO, model.getBairro());
        values.put(PedidoContrato.PedidoEntrada.COLUNA_CEP, model.getCep());
        values.put(PedidoContrato.PedidoEntrada.COLUNA_ENDERECO, model.getEndereco());
        values.put(PedidoContrato.PedidoEntrada.COLUNA_NUMERO, model.getNumero());
        values.put(PedidoContrato.PedidoEntrada.COLUNA_DATA, model.getData());

        String selection = PedidoContrato.PedidoEntrada.COLUNA_ID_PEDIDO + " = ? AND " + PedidoContrato.PedidoEntrada.COLUNA_ID_USUARIO + " = ?";
        String[] selectionArgs = { model.getIdPedido(), loginSharedPreferences.getId() };


        long newRowId =  db.update(
                PedidoContrato.PedidoEntrada.NOME_TABELA,
                values,
                selection,
                selectionArgs);


        if(newRowId == -1){
            return false;
        }

        return true;

    }


    public boolean excluir(PedidoModel model){
        LoginSharedPreferences loginSharedPreferences = new LoginSharedPreferences(contexto);

        SQLiteDatabase db = pedidoAjudante.getWritableDatabase();


        String selection = PedidoContrato.PedidoEntrada.COLUNA_ID_PEDIDO + " = ? AND " + PedidoContrato.PedidoEntrada.COLUNA_ID_USUARIO + " = ?";
        String[] selectionArgs = { model.getIdPedido(), loginSharedPreferences.getId() };


        long newRowId =  db.delete(
                PedidoContrato.PedidoEntrada.NOME_TABELA,
                selection,
                selectionArgs);


        if(newRowId == -1){
            return false;
        }

        return true;

    }

    public ArrayList<PedidoModel> getLista(){
        LoginSharedPreferences loginSharedPreferences = new LoginSharedPreferences(contexto);


        ArrayList<PedidoModel> lista = new ArrayList<>();

        SQLiteDatabase db = pedidoAjudante.getReadableDatabase();

        String[] projection = {
                PedidoContrato.PedidoEntrada.COLUNA_ID_PEDIDO,
                PedidoContrato.PedidoEntrada.COLUNA_ID_USUARIO,
                PedidoContrato.PedidoEntrada.COLUNA_BAIRRO,
                PedidoContrato.PedidoEntrada.COLUNA_NUMERO,
                PedidoContrato.PedidoEntrada.COLUNA_ENDERECO,
                PedidoContrato.PedidoEntrada.COLUNA_DATA,
                PedidoContrato.PedidoEntrada.COLUNA_CEP
        };


        String selection =  PedidoContrato.PedidoEntrada.COLUNA_ID_USUARIO + " = ?";


        String[] selectionArgs = { loginSharedPreferences.getId()};
        String ordenarPor =
                PedidoContrato.PedidoEntrada.COLUNA_ID_USUARIO+ " COLLATE NOCASE ASC";
        Cursor cursor = db.query(
                PedidoContrato.PedidoEntrada.NOME_TABELA,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                ordenarPor
        );



        if (cursor.moveToFirst()) {
            do {
                PedidoModel pedido = new PedidoModel();

                pedido.setBairro(cursor.getString(cursor.getColumnIndexOrThrow(PedidoContrato.PedidoEntrada.COLUNA_BAIRRO)));
                pedido.setCep(cursor.getString(cursor.getColumnIndexOrThrow(PedidoContrato.PedidoEntrada.COLUNA_CEP)));
                pedido.setData(cursor.getLong(cursor.getColumnIndexOrThrow(PedidoContrato.PedidoEntrada.COLUNA_DATA)));
                pedido.setEndereco(cursor.getString(cursor.getColumnIndexOrThrow(PedidoContrato.PedidoEntrada.COLUNA_ENDERECO)));
                pedido.setNumero(cursor.getString(cursor.getColumnIndexOrThrow(PedidoContrato.PedidoEntrada.COLUNA_NUMERO)));

                lista.add(pedido);
            } while (cursor.moveToNext());
        }
        db.close();

        return lista;

    }

}