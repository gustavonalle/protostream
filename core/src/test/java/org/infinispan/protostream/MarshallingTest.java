package org.infinispan.protostream;

import org.infinispan.protostream.domain.Account;
import org.infinispan.protostream.domain.Address;
import org.infinispan.protostream.domain.User;
import org.infinispan.protostream.test.AbstractProtoStreamTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author anistor@redhat.com
 */
public class MarshallingTest extends AbstractProtoStreamTest {

   @Test
   public void testMarshallUser() throws Exception {
      SerializationContext ctx = createContext();

      User user = new User();
      user.setId(1);
      user.setName("John");
      user.setSurname("Batman");
      user.setGender(User.Gender.MALE);
      user.setAccountIds(Arrays.asList(1, 3));
      user.setAddresses(Collections.singletonList(new Address("Old Street", "XYZ42")));

      byte[] bytes = ProtobufUtil.toByteArray(ctx, user);

      User decoded = ProtobufUtil.fromByteArray(ctx, bytes, User.class);

      assertEquals(1, decoded.getId());
      assertEquals("John", decoded.getName());
      assertEquals("Batman", decoded.getSurname());
      assertEquals(User.Gender.MALE, decoded.getGender());

      assertNotNull(decoded.getAddresses());
      assertEquals(1, decoded.getAddresses().size());
      assertEquals("Old Street", decoded.getAddresses().get(0).getStreet());
      assertEquals("XYZ42", decoded.getAddresses().get(0).getPostCode());

      assertNotNull(decoded.getAccountIds());
      assertEquals(2, decoded.getAccountIds().size());
      assertEquals(1, decoded.getAccountIds().get(0).intValue());
      assertEquals(3, decoded.getAccountIds().get(1).intValue());
   }

   @Test
   public void testMarshallAccount() throws Exception {
      SerializationContext ctx = createContext();

      Account account = new Account();
      account.setId(1);
      account.setDescription("test account");
      Date creationDate = new Date();
      account.setCreationDate(creationDate);
      List<byte[]> blurb = new ArrayList<byte[]>();
      blurb.add(new byte[0]);
      blurb.add(new byte[]{1, 2, 3});
      account.setBlurb(blurb);

      byte[] bytes = ProtobufUtil.toByteArray(ctx, account);

      Account decoded = ProtobufUtil.fromByteArray(ctx, bytes, Account.class);

      assertEquals(1, decoded.getId());
      assertEquals("test account", decoded.getDescription());
      assertEquals(creationDate, decoded.getCreationDate());

      assertNotNull(decoded.getBlurb());
      assertEquals(2, decoded.getBlurb().size());
      assertEquals(0, decoded.getBlurb().get(0).length);
      assertEquals(3, decoded.getBlurb().get(1).length);
      assertArrayEquals(new byte[]{1, 2, 3}, decoded.getBlurb().get(1));
   }
}
