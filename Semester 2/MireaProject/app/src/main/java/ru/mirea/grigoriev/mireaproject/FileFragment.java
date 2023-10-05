package ru.mirea.grigoriev.mireaproject;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ru.mirea.grigoriev.mireaproject.databinding.FragmentFileBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentFileBinding binding;
    private String text;

    public FileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileFragment newInstance(String param1, String param2) {
        FileFragment fragment = new FileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SecretKeySpec secretKeySpec;
        secretKeySpec = (SecretKeySpec) generateKey();
        binding.buttonEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = binding.editTextInputText.getText().toString();
                byte[] encodedText = encryptMsg(text,secretKeySpec);
                FileOutputStream outputStream;
                try {
                    outputStream = getActivity().openFileOutput(binding.editTextFilename.getText().toString(), Context.MODE_PRIVATE);
                    outputStream.write(encodedText);
                    outputStream.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        binding.buttonDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] text;
                text = getTextFromFile();
                String decodedText = decryptMsg(text,secretKeySpec);
                FileOutputStream outputStream;
                try {
                    outputStream = getActivity().openFileOutput(binding.editTextFilename.getText().toString(), Context.MODE_PRIVATE);
                    outputStream.write(decodedText.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.getTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] text;
                text = getTextFromFile();
                String finalText = new String(text);
                binding.textViewFileText.setText("Your text: "+ finalText);
            }
        });

        return view;
    }

    public byte[] getTextFromFile(){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = getActivity().openFileInput(binding.editTextFilename.getText().toString());
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            return bytes;
        }
        catch (IOException ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException ex) {
                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show(); }
        }
        return null;
    }

    public static SecretKey generateKey(){
        try	{
            SecureRandom sr	=	SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any	data	used	as	random	seed".getBytes());
            KeyGenerator kg	=	KeyGenerator.getInstance("AES");
            kg.init(256,	sr);
            return	new SecretKeySpec((kg.generateKey()).getEncoded(),	"AES");
            }
        catch	(NoSuchAlgorithmException e)	{
            throw	new	RuntimeException(e);
        }
    }

    public byte[] encryptMsg(String message, SecretKey secret)	{
        Cipher cipher	=	null;
        try	{
            cipher	=	Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE,	secret);
            Toast.makeText(getContext(),"Message encrypted", Toast.LENGTH_SHORT).show();
            return	cipher.doFinal(message.getBytes());
        }
        catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                       BadPaddingException | IllegalBlockSizeException e)	{
            throw new RuntimeException(e);
        }
    }

    public String	decryptMsg(byte[] cipherText, SecretKey secret){
        try	{
            Cipher	cipher	=	Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE,	secret);
            Toast.makeText(getContext(),"Message decrypted", Toast.LENGTH_SHORT).show();
            return	new	String(cipher.doFinal(cipherText));
        }	catch	(NoSuchAlgorithmException	|	NoSuchPaddingException|	IllegalBlockSizeException
                       |	BadPaddingException	|	InvalidKeyException	e)
        {
            throw	new	RuntimeException(e);
        }
    }
}