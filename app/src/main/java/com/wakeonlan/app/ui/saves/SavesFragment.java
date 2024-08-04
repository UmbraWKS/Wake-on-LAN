package com.wakeonlan.app.ui.saves;

import  android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wakeonlan.app.AuthSession;
import com.wakeonlan.app.AuthenticationActivity;
import com.wakeonlan.app.Files;
import com.wakeonlan.app.JsonParsing;
import com.wakeonlan.app.R;
import com.wakeonlan.app.adapters.AdapterSaveContent;
import com.wakeonlan.app.adapters.CustomAdapterSaves;
import com.wakeonlan.app.adapters.SwipeToDeleteCallback;
import com.wakeonlan.app.databinding.FragmentSavesBinding;
import com.wakeonlan.app.ui.home.HomeFragment;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Iterator;

public class SavesFragment extends Fragment implements AuthenticationActivity.CustomActions, CustomAdapterSaves.OnItemClickListener {

    private FragmentSavesBinding binding;
    protected static ArrayList<String> macs = new ArrayList<>();
    protected static ArrayList<String> names = new ArrayList<>();
    private CustomAdapterSaves saves;
    private static ArrayList<AdapterSaveContent> recyclerViewcontent = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentSavesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // initialize and bind data to recyclerview
        binding.itemsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        saves = new CustomAdapterSaves(requireContext(), recyclerViewcontent, this);
        binding.itemsList.setAdapter(saves);

        // adding separator between items
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        binding.itemsList.addItemDecoration(itemDecoration);

        // setting up the callback to delete an item by swiping
        ItemTouchHelper.SimpleCallback callback = new SwipeToDeleteCallback(saves, requireContext());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.itemsList);

        // if user is not authenticated it launches auth activity
        // is user doesn't have an auth system active it is not required to authenticate
        KeyguardManager keyguardManager = (KeyguardManager) requireContext().getSystemService(Context.KEYGUARD_SERVICE);
        if(!AuthSession.isUserAuthenticated(requireActivity()) && keyguardManager.isDeviceSecure()) {
            Intent authIntent = new Intent(requireContext(), AuthenticationActivity.class);
            AuthenticationActivity.callback = this; // static callback for the interface
            startForResultActivityLauncher.launch(authIntent);// start activity with required result
        }else // for devices not supporting the auth system and devices without an authentication method setup
            afterAuthActions(); // granted the auth by default manually


        binding.addMacAddress.setOnClickListener(this::onClickAddAddress);

        return view;
    }

    private void onClickAddAddress(View view) {
        Intent createSave = new Intent(requireContext(), CreateSaveActivity.class);
        startActivity(createSave);
    }


    // loading data from file
    private void loadData(){
        JSONArray jsonArray = Files.readFile(requireContext());
        try {
            ArrayList<ArrayList<String>> data = JsonParsing.fromJson(jsonArray);
            if(data != null) {
                macs = data.get(0);
                names = data.get(1);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Iterator<String> macIterator = macs.iterator();
        Iterator<String> nameIterator = names.iterator();

        while(macIterator.hasNext() && nameIterator.hasNext()){
            AdapterSaveContent items = new AdapterSaveContent();
            items.setMac(macIterator.next());
            items.setName(nameIterator.next());
            recyclerViewcontent.add(items);
        }

        saves.notifyDataSetChanged();

    }

    // what happens after a successful authentication
    @Override
    public void afterAuthActions() {
        // reload the data from the file
        if(recyclerViewcontent.isEmpty())
            loadData();
        else // or update with the new data
            saves.notifyDataSetChanged();
    }

    // adding the data in the recyclerViewContent to dynamically update the list
    // i don't have to re-read the file to update the data every time
    // the operations of file writing must be done after this method is called
    public static void addData(String mac, String name){
        macs.add(mac);
        names.add(name);
        recyclerViewcontent.add(new AdapterSaveContent(mac, name));
    }

    // i need to get the result of the auth activity when it ends to know what to do
    private final ActivityResultLauncher<Intent> startForResultActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_CANCELED)
                    switchToHomeFragment(); // if the user doesn't authenticate the fragment is switched to the home
                // for more security and better user experience
            });

    // goes back to home fragment
    private void switchToHomeFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        // i use the navController since it's the navigation method applied in the main activity
        // and use the navigation route setup in the mobile_navigation.xml file
        navController.navigate(R.id.action_navigation_saves_to_navigation_home);
    }

    @Override
    public void onResume() {
        super.onResume();
        saves.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        HomeFragment.macAddressField = recyclerViewcontent.get(position).getMac();
        switchToHomeFragment();
    }
}
