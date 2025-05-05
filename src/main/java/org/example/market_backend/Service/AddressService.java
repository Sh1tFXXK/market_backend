package org.example.market_backend.Service;

import org.example.market_backend.Entity.Address;
import org.example.market_backend.VO.AddressVO;
import org.example.market_backend.VO.BaseVO;

import java.util.List;

public interface AddressService {
    List<Address> getAddressList(BaseVO vo);

    void saveAddress(AddressVO vo);

    void updateAddress(AddressVO vo);

    void delAddress(AddressVO vo);
}
