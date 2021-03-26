package handler;

public class RegInfo {
    String username, password;

    @Override
    public boolean equals(Object obj) {
        RegInfo regInfo = (RegInfo) obj;

        return this.username.equals(regInfo.username) && this.password.equals(regInfo.password);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((username == null) ? 0 : username.hashCode());
        return result;
    }
}
