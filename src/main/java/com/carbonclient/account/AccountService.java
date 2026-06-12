package com.carbonclient.account;

import java.util.Optional;

public interface AccountService {

    Optional<AccountProfile> getCurrentAccount();
}
