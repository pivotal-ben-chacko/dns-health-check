**BoshDNS Health Checker**

****Usage****

Resolve a hostname from the Diego cell once

    https://<route-to-app>/resolve/<hostname>

****Ex****

    https://<route-to-app>/resolve/google.com

Resolve a hostname from the Diego cell n number times

    https://<route-to-app>/resolve/<hostname>/<count>

****Ex****

    https://<route-to-app>/resolve/google.com/10
    
Resolve a hostname from the Diego cell multiple times indefinately or up to 5min

    https://<route-to-app>/loop/<hostname>

****Ex****

    https://<route-to-app>/loop/google.com
    
In order to stop the loop call the stop endpoint

    https://<route-to-app>/loop/stop

